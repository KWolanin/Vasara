// src/boot/customDirectives.js
import { quotes } from "src/quotesConfig";

export default ({ app }) => {
  app.directive("smart-quotes", {
    mounted(el) {
      const currentLang = "pl";
      el.addEventListener("input", async (event) => {
        const quote = quotes[currentLang];
        if (quote) {
          const sel = window.getSelection();
          const node = sel.focusNode;
          const offset = sel.focusOffset;
          const pos = getCursorPosition(el, node, offset, {
            pos: 0,
            done: false,
          });
          if (offset === 0) pos.pos += 0.5;

          event.target.innerHTML = event.target.innerHTML.replace(
            /"([^"]*)"/g,
            `${quote.open}$1${quote.close}`
          );

          sel.removeAllRanges();
          const range = setCursorPosition(el, document.createRange(), {
            pos: pos.pos,
            done: false,
          });
          range.collapse(true);
          sel.addRange(range);
        }
      });
    },
  });
};

function getCursorPosition(parent, node, offset, stat) {
  if (stat.done) return stat;

  let currentNode = null;
  if (parent.childNodes.length == 0) {
    stat.pos += parent.textContent.length;
  } else {
    for (let i = 0; i < parent.childNodes.length && !stat.done; i++) {
      currentNode = parent.childNodes[i];
      if (currentNode === node) {
        stat.pos += offset;
        stat.done = true;
        return stat;
      } else getCursorPosition(currentNode, node, offset, stat);
    }
  }
  return stat;
}

function setCursorPosition(parent, range, stat) {
  if (stat.done) return range;

  let currentNode = null;
  if (parent.childNodes.length == 0) {
    if (parent.textContent.length >= stat.pos) {
      range.setStart(parent, stat.pos);
      stat.done = true;
    } else {
      stat.pos = stat.pos - parent.textContent.length;
    }
  } else {
    for (let i = 0; i < parent.childNodes.length && !stat.done; i++) {
      currentNode = parent.childNodes[i];
      setCursorPosition(currentNode, range, stat);
    }
  }
  return range;
}
