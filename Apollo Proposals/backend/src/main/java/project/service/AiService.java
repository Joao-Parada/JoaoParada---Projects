package project.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.tika.Tika;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.dto.RequestDto;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AiService {
    @Value("${openai.api.key}")
    private String apiKey;

    @Value("classpath:templates/promptAi.st")
    private Resource functionPromptTemplate;
    private SimpleVectorStore vectorStore;

    private final ChatClient chatClient;

    public AiService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public Generation getChatResponse(RequestDto requestContent, List<File> files) {
        List<String> filesContent = new ArrayList<>();

        // Se arquivos forem enviados, atualiza o vetor e faz a busca
        if (files != null && !files.isEmpty()) {
            updateVectorStore(files); // Atualiza o vetor com os novos arquivos
            filesContent = searchInVectorStore(requestContent.toString(), files.size());
        } else {
            System.out.println("Nenhum arquivo foi enviado. Processando apenas o RequestDto.");
        }

        // Cria o prompt a partir do template
        PromptTemplate promptTemplate = new PromptTemplate(functionPromptTemplate);
        Prompt prompt = promptTemplate.create(Map.of(
                "input", requestContent,
                "documents", String.join("\n", filesContent)
        ));

        // Realiza a chamada ao ChatClient
        return chatClient.call(prompt).getResult();
    }

    public void updateVectorStore(List<File> files) {
        Tika tika = new Tika();
        for (File file : files) {
            if (file.exists()) {
                System.out.println("Carregando vetor de: " + file.getName());
                vectorStore.load(file);

                try (InputStream inputStream = new FileInputStream(file)) {
                    String rawContent = tika.parseToString(inputStream);
                    Document doc = new Document(rawContent);
                    List<Document> docs = List.of(doc);

                    TextSplitter textSplitter = new TokenTextSplitter();
                    List<Document> splitDocs = textSplitter.apply(docs);

                    vectorStore.add(splitDocs);
                    vectorStore.save(file);

                } catch (Exception e) {
                    System.err.println("Erro ao processar arquivo com Tika: " + file.getName());
                    e.printStackTrace();
                }
            } else {
                System.out.println("Arquivo não encontrado: " + file.getAbsolutePath());
            }
        }
    }

    public List<String> searchInVectorStore(String searchParam, int filesSize) {
        List<Document> documents = vectorStore.similaritySearch(SearchRequest.query(searchParam).withTopK(filesSize));
        return documents.stream().map(Document::getContent).toList();
    }

    public byte[] generatePdf(String content) {
        try (PDDocument document = new PDDocument()) {
            // Create a new blank page
            PDPage page = new PDPage();
            document.addPage(page);

            // Create a content stream to write text into the page
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA, 12); // Set font and size
                contentStream.beginText();
                contentStream.setLeading(14.5f); // Set line spacing
                contentStream.newLineAtOffset(50, 750); // Set starting position (x=50, y=750)

                // Split content into lines if it's too long
                String[] lines = content.split("\n");
                for (String line : lines) {
                    contentStream.showText(line); // Write a line of text
                    contentStream.newLine(); // Move to the next line
                }

                contentStream.endText(); // End the text block
            }

            // Write the PDF to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray(); // Return PDF as byte array
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }
}