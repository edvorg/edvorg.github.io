(ns edvorg.figwheel
  (:require [figwheel-sidecar.repl-api :as ra]
            [edvorg.config :as config]
            [mount.core :as mount]))

(mount/defstate  ^{:on-reload :noop} figwheel
  :start (if (= :dev (:mode @config/env))
           (do
             (ra/start-figwheel!)
             {:active true})
           {:active false})
  :stop (when (:active @figwheel)
          (ra/stop-figwheel!)))
