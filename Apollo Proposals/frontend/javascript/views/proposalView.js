import proposalService from "../services/proposalService.js";

function renderPage() {
  const container = document.getElementById("container");

  container.innerHTML = `
    <header class="scrollreveal-header">
        <div class="interface">
            <div class="logo">
                <a href="homeView.html">
                    <img class="logo" src="/resources/apollo-logo.png" alt="logo" />
                </a>
            </div>
            <nav class="menu-desktop">
               <ul>
                    <li><a href="/home">Home</a></li>
                    <li><a href="/history">History</a></li>
                    <li><a href="/" class="logout-link">LogOut</a></li>
                    <li>
                      <a href="proposal.html">
                        <button class="generate-button">Generate Proposal</button>
                      </a>
                    </li>
                </ul>
            </nav>
        </div>
    </header>

    <main>
        <section class="proposal-form">
            <div class="interface">
              <div class="form-box">
                <h2>Submit a New Request</h2>
                <form id="proposal-form" enctype="multipart/form-data">
                  <input type="text" name="projectName" placeholder="Company Name?" required />
                  <select name="sector" required>
                    <option value="">Select Sector</option>
                    <option value="Architecture">Architecture</option>
                    <option value="Sales">Sales</option>
                    <option value="Marketing">Marketing</option>
                    <option value="Shoes">Shoes</option>
                    <option value="Car Dealer">Car Dealer</option>
                    <option value="Healthcare">Healthcare</option>
                    <option value="Education">Education</option>
                    <option value="Technology">Technology</option>
                  </select>
                  <textarea name="generalDescription" placeholder="General Description" required></textarea>
                  <textarea name="problems" placeholder="Problems to solve" required></textarea>
                  <textarea name="typeOfServices" placeholder="Type of Services you want" required></textarea>
                  <textarea name="expectedFeatures" placeholder="Expected Features" required></textarea>
                  <textarea name="preferredTechnologies" placeholder="Preferred Technologies" required></textarea>
                  <textarea name="restrictionsOrRequests" placeholder="Restrictions or Requests"></textarea>
                  <input type="number" name="budget" placeholder="Budget" required />
                  <p>Deadline:</p>
                  <input type="date" name="deadline" required />
                  <textarea name="additionalComments" placeholder="Additional Comments"></textarea>
                  <label for="documents">Additional Documents:</label>
                  <input type="file" name="documents" id="documents" multiple />
                  <button type="submit">Submit</button>
                </form>
                <div id="result-message"></div>
              </div>
            </div>
        </section>
    </main>
  `;

  setTimeout(() => {
    const form = document.getElementById("proposal-form");
    if (form) {
      form.addEventListener("submit", function (e) {
        e.preventDefault();
        proposalService.submitProposal(form);
      });
    }
  }, 0);
}

export default { renderPage };
