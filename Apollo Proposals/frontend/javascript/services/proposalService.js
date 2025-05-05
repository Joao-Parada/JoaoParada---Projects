const proposalService = {
  async submitProposal(formElement) {
    const formData = new FormData(formElement);

    try {
      const response = await fetch('http://ec2-13-51-242-135.eu-north-1.compute.amazonaws.com:8080/apollo-proposals-0.0.1/api/generate-proposal', {
        method: 'POST',
        body: formData,
        mode: 'cors'
      });

      if (!response.ok) {
        const error = await response.text();
        throw new Error(error);
      }

      const result = await response.json(); // expecting result.text and result.requestId

      // Create Modal
      const modalHTML = `
        <div class="custom-modal-overlay">
          <div class="custom-modal">
            <h2>Proposal Submitted Successfully</h2>
            <p>${result.text}</p>
            <div class="modal-actions">
              <button id="approve-btn">Approve</button>
              <button id="save-btn">Save for Later</button>
              <button id="reject-btn">Reject</button>
            </div>
          </div>
        </div>
      `;

      document.body.insertAdjacentHTML('beforeend', modalHTML);

      // Setup button handlers
      document.getElementById("approve-btn").addEventListener("click", () => {
        proposalService.sendAction('approve', result.requestId);
        closeModal();
      });

      document.getElementById("save-btn").addEventListener("click", () => {
        proposalService.sendAction('save', result.requestId);
        closeModal();
      });

      document.getElementById("reject-btn").addEventListener("click", () => {
        proposalService.sendAction('reject', result.requestId);
        closeModal();
      });

      function closeModal() {
        const modal = document.querySelector(".custom-modal-overlay");
        if (modal) modal.remove();
      }

      // Optionally reset the form
      formElement.reset();

      return result;
    } catch (error) {
      alert("Submission failed: " + error.message);
    }
  },

  async sendAction(action, requestId) {
    const url = `http://ec2-13-51-242-135.eu-north-1.compute.amazonaws.com:8080/apollo-proposals-0.0.1/api/proposal/${action}`;

    try {
      const res = await fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ requestId })
      });

      if (!res.ok) {
        const errorText = await res.text();
        throw new Error(errorText);
      }

      const result = await res.text();
      alert(`Action '${action}' completed: ${result}`);
    } catch (err) {
      alert(`Failed to ${action} request: ${err.message}`);
    }
  }
};

export default proposalService;
