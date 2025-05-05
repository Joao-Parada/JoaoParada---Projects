const historyService = {
  async fetchProposals() {
    try {
      const response = await fetch(
        "http://localhost:8080/apollo-proposals/api/requestsproposals",
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (!response.ok) {
        throw new Error("Failed to fetch proposals");
      }

      const proposals = await response.json();
      return proposals;

    } catch (error) {
      console.error("Error fetching proposals:", error);
      return [];
    }
  },

  async deleteProposal(id) {
    try {
      const response = await fetch(
        `http://ec2-13-51-242-135.eu-north-1.compute.amazonaws.com:8080/apollo-proposals-0.0.1/api/requests/${id}`,
        {
          method: "DELETE",
        }
      );

      if (!response.ok) {
        throw new Error("Failed to delete proposal");
      }

      return true;

    } catch (error) {
      console.error("Error deleting proposal:", error);
      return false;
    }
  }
};

export default historyService;
