(ns edvorg.view.editor
  (:require [rocks.clj.configuron.core :refer [env]]
            [reagent.core :refer [cursor]]
            [taoensso.timbre :as timbre]))

(def tile-urls {"/img/tiles/tree/left.png"      {:order 2 :size [1 7]}
                "/img/tiles/tree/center.png"    {:order 2 :size [1 7]}
                "/img/tiles/tree/right-end.png" {:order 2 :size [1 7]}
                "/img/tiles/tree/single.png"    {:order 2 :size [1 6]}
                "/img/tiles/tree/left-end.png"  {:order 2 :size [1 5]}

                "/img/tiles/fences/bottom-right-decor.png" {:order 1 :size [1 1]}
                "/img/tiles/fences/bottom-left.png"        {:order 2 :size [1 1]}
                "/img/tiles/fences/bottom.png"             {:order 2 :size [1 1]}
                "/img/tiles/fences/left.png"               {:order 2 :size [1 1]}
                "/img/tiles/fences/bottom-right.png"       {:order 2 :size [1 1]}
                "/img/tiles/fences/right-decor.png"        {:order 1 :size [1 1]}
                "/img/tiles/fences/right.png"              {:order 2 :size [1 1]}

                "/img/tiles/grass/tile-3.png" {:order 0 :size [1 1]}
                "/img/tiles/grass/tile-2.png" {:order 0 :size [1 1]}
                "/img/tiles/grass/tile-1.png" {:order 0 :size [1 1]}

                "/img/tiles/road/bottom.png"               {:order 1 :size [1 1]}
                "/img/tiles/road/top-end.png"              {:order 1 :size [1 1]}
                "/img/tiles/road/bottom-end.png"           {:order 1 :size [1 1]}
                "/img/tiles/road/left.png"                 {:order 1 :size [1 1]}
                "/img/tiles/road/top.png"                  {:order 1 :size [1 1]}
                "/img/tiles/road/right-end.png"            {:order 1 :size [1 1]}
                "/img/tiles/road/crossing.png"             {:order 1 :size [1 1]}
                "/img/tiles/road/left-end.png"             {:order 1 :size [1 1]}
                "/img/tiles/road/horizontal-damaged-2.png" {:order 1 :size [1 1]}
                "/img/tiles/road/right.png"                {:order 1 :size [1 1]}
                "/img/tiles/road/horizontal-damaged-1.png" {:order 1 :size [1 1]}

                "/img/tiles/objects/flowers.png" {:order 2 :size [1 1]}
                "/img/tiles/objects/chest.png"   {:order 2 :size [1 1]}
                "/img/tiles/objects/barrel.png"  {:order 2 :size [1 1]}

                "/img/tiles/soil/bottom-left.png"  {:order 1 :size [1 1]}
                "/img/tiles/soil/bottom.png"       {:order 1 :size [1 1]}
                "/img/tiles/soil/left.png"         {:order 1 :size [1 1]}
                "/img/tiles/soil/top-left.png"     {:order 1 :size [1 1]}
                "/img/tiles/soil/top.png"          {:order 1 :size [1 1]}
                "/img/tiles/soil/bottom-right.png" {:order 1 :size [1 1]}
                "/img/tiles/soil/center.png"       {:order 1 :size [1 1]}
                "/img/tiles/soil/top-right.png"    {:order 1 :size [1 1]}
                "/img/tiles/soil/pumpkin.png"      {:order 1 :size [1 1]}
                "/img/tiles/soil/right.png"        {:order 1 :size [1 1]}

                "/img/tiles/rock/big.png"   {:order 2 :size [1 1]}
                "/img/tiles/rock/small.png" {:order 2 :size [1 1]}

                "/img/tiles/house/main.png"          {:order 2 :size [6 8]}
                "/img/tiles/house/shadow-corner.png" {:order 1 :size [1 1]}
                "/img/tiles/house/secondary.png"     {:order 2 :size [3 9]}
                "/img/tiles/house/extension.png"     {:order 2 :size [3 4]}
                "/img/tiles/house/shadow.png"        {:order 1 :size [1 1]}

                "/img/tiles/lake/bottom.png"       {:order 1 :size [1 1]}
                "/img/tiles/lake/left-bottom.png"  {:order 1 :size [1 1]}
                "/img/tiles/lake/left-top.png"     {:order 1 :size [1 1]}
                "/img/tiles/lake/right-bottom.png" {:order 1 :size [1 1]}
                "/img/tiles/lake/right-top.png"    {:order 1 :size [1 1]}
                "/img/tiles/lake/top.png"          {:order 1 :size [1 1]}

                "/img/tiles/waterfall/bottom.png" {:order 1 :size [1 1]}
                "/img/tiles/waterfall/center.png" {:order 1 :size [1 1]}
                "/img/tiles/waterfall/top.png"    {:order 1 :size [1 1]}

                "/img/tiles/river/crossing.png"     {:order 1 :size [1 1]}
                "/img/tiles/river/horizontal.png"   {:order 1 :size [1 1]}
                "/img/tiles/river/left-bottom.png"  {:order 1 :size [1 1]}
                "/img/tiles/river/left-top.png"     {:order 1 :size [1 1]}
                "/img/tiles/river/right-bottom.png" {:order 1 :size [1 1]}
                "/img/tiles/river/right-top.png"    {:order 1 :size [1 1]}
                "/img/tiles/river/vertical.png"     {:order 1 :size [1 1]}

                "/img/tiles/cliff/left-end.png" {:order 1 :size [1 4]}
                "/img/tiles/cliff/right-end.png" {:order 1 :size [1 5]}
                "/img/tiles/cliff/left.png"     {:order 1 :size [1 3]}
                "/img/tiles/cliff/right.png"    {:order 1 :size [1 3]}})

(defn to-absolute [[x y]]
  (let [{[w h] :tile-ui-size} env]
    [(* x w) (* y h)]))

(defn npc-view [url pos]
  (let [[width height] (to-absolute [1 1])
        [x y] (to-absolute pos)]
    [:div.npc.npc-walk-right {:style {:background-image (str "url(\"" url "\")");
                                      :width width
                                      :height height
                                      :left x
                                      :top y
                                      :z-index 2}}]))

(defn tile-view [url pos size order]
  (let [[width height] (to-absolute size)
        [x y] (to-absolute pos)]
    [:div.tile {:style {:background-image (str "url(\"" url "\")")
                        :width width
                        :height height
                        :left x
                        :top y
                        :z-index order}}]))

(defn tile-placeholder [pos size active-tile tile-x-y active-tool]
  (let [[width height] (to-absolute size)
        [x y] (to-absolute pos)]
    [:div.tile-placeholder {:style {:width width
                                    :height height
                                    :left x
                                    :top y}
                            :on-click (fn [_]
                                        (case @active-tool
                                          :draw  (when-let [url @active-tile]
                                                   (let [{:keys [order]} (get tile-urls url)]
                                                     (swap! tile-x-y (fn [urls]
                                                                       (if (get urls url)
                                                                         (disj urls url)
                                                                         (conj (set urls) url))))))
                                          :clear (reset! tile-x-y #{})))}]))

(defn editor-tile-pic [url active-tile]
  (let [{[width height] :tile-ui-size} env]
    [:div.editor-tile-pic.pixelated {:class (when (= url @active-tile)
                                                 "editor-tile-pic-active")
                                     :on-click (fn [_]
                                                 (reset! active-tile url))
                                     :title url}
     [:div.editor-tile-pic-inner {:style {:width  width
                                          :height height}}
      [tile-view url [0 0] [1 1] 0]]]))

(defn game-tile-view [pos tile-x-y]
  [:div.game-tile
   (for [url @tile-x-y]
     (let [{:keys [size order]} (get tile-urls url)]
       ^{:key order}
       [tile-view url pos size order]))])

(defn view [state]
  (let [active-tile (cursor state [:editor :active-tile])
        active-tool (cursor state [:editor :active-tool])
        tiles (cursor state [:tiles])
        {[width height] :window-size
         [tile-width tile-height] :tile-ui-size} env]
    [:div
     [:div.playground.pixelated {:style {:width width
                                         :height height}}
      [npc-view "/img/spritesheets/npc-1.png" [0 0]]
      [npc-view "/img/spritesheets/npc-2.png" [1 0]]
      [npc-view "/img/spritesheets/npc-3.png" [2 0]]
      (for [x (range 0 (/ width tile-width))
            y (range 0 (/ height tile-height))]
        (let [x (int x)
              y (int y)
              tile-x-y (cursor tiles [x y])]
          ^{:key (str x "-" y)}
          [:div {:style {:position :relative}}
           [game-tile-view [x y] tile-x-y]
           [tile-placeholder [x y] [1 1] active-tile tile-x-y active-tool]]))]
     [:h5 "Tiles"]
     [:div
      (for [tile-url (->> tile-urls
                          (sort-by first)
                          (sort-by (comp :order second))
                          (map first))]
        ^{:key tile-url}
        [editor-tile-pic tile-url active-tile])]
     [:h5 "Tools"]
     [:div
      [:a {:href ""
           :on-click (fn [_]
                       (reset! active-tool :clear))}
       "Clear"]]
     [:div
      [:a {:href ""
           :on-click (fn [_]
                       (reset! active-tool :draw))}
       "Draw"]]]))
