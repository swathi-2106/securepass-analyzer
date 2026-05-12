(function () {
    const themeToggle = document.querySelector("#themeToggle");
    const savedTheme = localStorage.getItem("securepass-theme");
    if (savedTheme === "light") {
        document.body.classList.add("light");
    }

    themeToggle?.addEventListener("click", () => {
        document.body.classList.toggle("light");
        localStorage.setItem("securepass-theme", document.body.classList.contains("light") ? "light" : "dark");
    });

    const passwordInput = document.querySelector("#passwordInput");
    if (!passwordInput) {
        return;
    }

    const revealButton = document.querySelector("#revealButton");
    const copyButton = document.querySelector("#copyButton");
    const refreshSuggestion = document.querySelector("#refreshSuggestion");
    const generatedPassword = document.querySelector("#generatedPassword");
    const scoreBar = document.querySelector("#scoreBar");
    const scoreText = document.querySelector("#scoreText");
    const strengthLabel = document.querySelector("#strengthLabel");
    const entropyValue = document.querySelector("#entropyValue");
    const weakValue = document.querySelector("#weakValue");
    const dictionaryValue = document.querySelector("#dictionaryValue");
    const breachValue = document.querySelector("#breachValue");
    const warningsList = document.querySelector("#warningsList");
    const suggestionsList = document.querySelector("#suggestionsList");
    const toast = document.querySelector("#toast");
    let debounceTimer;

    revealButton?.addEventListener("click", () => {
        passwordInput.type = passwordInput.type === "password" ? "text" : "password";
    });

    copyButton?.addEventListener("click", async () => {
        if (!passwordInput.value) {
            showToast("Nothing to copy yet.");
            return;
        }
        await navigator.clipboard.writeText(passwordInput.value);
        showToast("Password copied.");
    });

    refreshSuggestion?.addEventListener("click", async () => {
        const response = await fetch("/api/suggest?length=14", { method: "POST", headers: csrfHeaders() });
        generatedPassword.value = await response.text();
        showToast("New password generated.");
    });

    passwordInput.addEventListener("input", () => {
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(analyzePassword, 220);
    });

    async function analyzePassword() {
        const password = passwordInput.value;
        if (!password.trim()) {
            renderEmptyState();
            return;
        }

        scoreBar.classList.add("loading");
        try {
            const response = await fetch("/api/analyze", {
                method: "POST",
                headers: { "Content-Type": "application/json", ...csrfHeaders() },
                body: JSON.stringify({ password })
            });

            if (!response.ok) {
                throw new Error("Unable to analyze password.");
            }

            renderAnalysis(await response.json());
        } catch (error) {
            showToast(error.message);
        } finally {
            scoreBar.classList.remove("loading");
        }
    }

    function renderAnalysis(data) {
        scoreBar.style.width = `${data.score}%`;
        scoreBar.style.background = scoreColor(data.score);
        scoreText.textContent = `${data.score}/100`;
        strengthLabel.textContent = labelFor(data.strength);
        entropyValue.textContent = `${data.entropy} bits`;
        weakValue.textContent = data.weakPassword ? "Danger" : "Clear";
        dictionaryValue.textContent = data.dictionaryPattern ? "Detected" : "Clear";
        breachValue.textContent = data.breachedPassword ? "Leaked" : "Clear";
        generatedPassword.value = data.generatedPassword;
        renderList(warningsList, data.warnings);
        renderList(suggestionsList, data.suggestions.length ? data.suggestions : data.passedChecks);
    }

    function renderEmptyState() {
        scoreBar.style.width = "0";
        scoreText.textContent = "0/100";
        strengthLabel.textContent = "Awaiting input";
        entropyValue.textContent = "0 bits";
        weakValue.textContent = "Clear";
        dictionaryValue.textContent = "Clear";
        breachValue.textContent = "Clear";
        renderList(warningsList, ["No scan yet."]);
        renderList(suggestionsList, ["Use at least 12 characters with mixed character classes."]);
    }

    function renderList(target, items) {
        target.replaceChildren();
        items.forEach((item) => {
            const li = document.createElement("li");
            li.textContent = item;
            target.appendChild(li);
        });
    }

    function scoreColor(score) {
        if (score < 25) return "var(--danger)";
        if (score < 45) return "#ff8f3d";
        if (score < 65) return "var(--warning)";
        if (score < 85) return "var(--accent-2)";
        return "var(--ok)";
    }

    function labelFor(strength) {
        return String(strength || "VERY_WEAK").toLowerCase().split("_").map((part) => part.charAt(0).toUpperCase() + part.slice(1)).join(" ");
    }

    function csrfHeaders() {
        const token = document.querySelector("meta[name='_csrf']")?.content;
        const header = document.querySelector("meta[name='_csrf_header']")?.content;
        return token && header ? { [header]: token } : {};
    }

    function showToast(message) {
        if (!toast) {
            return;
        }
        toast.textContent = message;
        toast.classList.add("show");
        setTimeout(() => toast.classList.remove("show"), 2200);
    }
})();
