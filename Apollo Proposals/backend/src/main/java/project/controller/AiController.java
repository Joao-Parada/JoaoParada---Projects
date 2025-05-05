package project.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.dto.RequestDto;
import project.service.AiService;
import project.service.RequestService;

import java.math.BigDecimal;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;
    private final RequestService requestService;

    public AiController(AiService aiService, RequestService requestService) {
        this.aiService = aiService;
        this.requestService = requestService;
    }

    @PostMapping("/{customerId}/generate-proposal")
    public ResponseEntity<byte[]> generateProposal(
            @PathVariable Long customerId,
            @RequestBody RequestDto requestDto,
            @RequestParam(value = "files", required = false) List<MultipartFile> files // Arquivos opcionais
    ) {
        try {
            // Setando o customerId no requestDto
            requestDto.setCustomerId(customerId);

            // Converte os arquivos recebidos para File, se existir
            List<File> fileList = (files != null && !files.isEmpty())
                    ? convertMultipartFilesToFileList(files)
                    : new ArrayList<>();

            // Gera a resposta do AI
            String response = aiService.getChatResponse(requestDto, fileList).getOutput().getContent();
            requestDto.setResponse(response);

            requestService.createRequest(requestDto);

            byte[] pdfContent = aiService.generatePdf(response);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=proposal.pdf") // Makes the browser download the file
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfContent);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error processing the request: " + e.getMessage()).getBytes());
        }
    }

    private List<File> convertMultipartFilesToFileList(List<MultipartFile> files) {
        return files.stream()
                .map(file -> {
                    try {
                        // Certifica-se de que o caminho do arquivo esteja correto
                        File convertedFile = new File("src/main/resources/files/" + file.getOriginalFilename());

                        // Cria o diretório se ele não existir
                        convertedFile.getParentFile().mkdirs();

                        // Salva o arquivo no diretório
                        file.transferTo(convertedFile);
                        System.out.println("Arquivo salvo: " + convertedFile.getAbsolutePath());
                        return convertedFile;

                    } catch (Exception e) {
                        System.err.println("Erro ao converter o arquivo: " + file.getOriginalFilename());
                        e.printStackTrace();
                        throw new RuntimeException("Erro ao converter arquivo.", e);
                    }
                })
                .collect(Collectors.toList());
    }
}