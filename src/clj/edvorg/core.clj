(ns edvorg.core
  (:gen-class)
  (:require [clojure.java.io :as io]
            [figwheel-sidecar.repl-api :as ra]
            [edvorg.figwheel :refer [figwheel]]
            [edvorg.server :refer [server]]
            [mount.core :as mount]
            [taoensso.timbre :as timbre]))

(defn cljs-repl []
  (ra/cljs-repl))

(defn start []
  (mount/start))

(defn stop []
  (mount/stop))

(defn reset []
  (stop)
  (start))

(mount/in-cljc-mode)

(defn -main [& args]
  (start)
  (timbre/info "server status" @server)
  (timbre/info "figwheel status" @figwheel))
