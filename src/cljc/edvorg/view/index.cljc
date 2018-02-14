(ns edvorg.view.index)

(defn view
  "Index page."
  [state]
  ;; Cursors are used for reactive rendering.
  ;; When data changes only the views that are affected by change will be re-rendered.
  [:div.playground
   (let [{:keys [ground-tiles]} @state]
     (for [[i row]  (map-indexed vector ground-tiles)
           [j tile] (map-indexed vector row)]
       ^{:key (str i "-" j)}
       [:div.tile {:class (name tile)
                   :style {:left (str (* 16 j) "px")
                           :top (str (* 16 i) "px")}}]))])
