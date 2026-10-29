(() => {
  "use strict";

  document.querySelectorAll(".main-content pre > code").forEach((code) => {
    const pre = code.parentElement;
    const block = document.createElement("div");
    block.className = "example-code-block";
    pre.before(block);

    const controls = document.createElement("div");
    controls.className = "copy-controls";

    const status = document.createElement("span");
    status.className = "copy-status";
    status.setAttribute("role", "status");

    const button = document.createElement("button");
    button.type = "button";
    button.className = "btn";
    button.textContent = "Copia";
    button.setAttribute("aria-label", "Copia esempio negli appunti");

    controls.append(status, button);
    block.append(controls, pre);

    let timeout;
    button.addEventListener("click", async () => {
      window.clearTimeout(timeout);
      status.textContent = "";
      button.disabled = true;

      try {
        await navigator.clipboard.writeText(code.textContent);
        status.textContent = "Copiato!";
      } catch {
        status.textContent = "Copia non riuscita. Seleziona e copia il testo manualmente.";
      } finally {
        button.disabled = false;
        timeout = window.setTimeout(() => {
          status.textContent = "";
        }, 5000);
      }
    });
  });
})();
