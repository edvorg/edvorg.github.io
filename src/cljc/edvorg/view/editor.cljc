(ns edvorg.view.editor)

(defn view
  "Editor page."
  [state]
  ;; Cursors are used for reactive rendering.
  ;; When data changes only the views that are affected by change will be re-rendered.
  [:div.playground.pixelated])
