(ns edvorg.util)

(defn socket-url [host port]
  (str "ws://" host ":" port "/ws"))
