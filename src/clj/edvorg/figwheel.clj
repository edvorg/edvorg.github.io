(ns edvorg.figwheel
  (:require [figwheel-sidecar.repl-api :as ra]
            [rocks.clj.configuron.core :refer [env]]
            [mount.core :as mount]))

(mount/defstate  ^{:on-reload :noop} figwheel
  :start (if (= :dev (:mode env))
           (do
             (ra/start-figwheel!)
             {:active true})
           {:active false})
  :stop (when (:active @figwheel)
          (ra/stop-figwheel!)))
