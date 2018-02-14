(ns edvorg.server
  (:require [immutant.web :refer [run stop]]
            [immutant.web.undertow :refer [options]]
            [rocks.clj.configuron.core :refer [env]]
            [edvorg.handler :as handler]
            [mount.core :as mount]
            [taoensso.timbre :as timbre]))

(mount/defstate ^{:on-reload :noop} server
  :start (let [{:keys [mode host port ssl-port keystore key-password io-threads worker-threads]
                :or {host "0.0.0.0"
                     io-threads 2}} env
               params (cond-> {:host host}
                        io-threads (assoc :io-threads io-threads)
                        worker-threads (assoc :worker-threads worker-threads)
                        ssl-port (assoc :ssl-port (str ssl-port)
                                        :keystore keystore
                                        :key-password key-password
                                        :client-auth :need)
                        port (assoc :port (str port)))
               handler (case (:mode env)
                         :uberjar #'handler/prod-routes
                         :dev #'handler/dev-routes)]
           (timbre/info "starting server in" mode "mode with params" params)
           {:server (run handler (options params))})
  :stop (let [{:keys [server]} @server]
          (timbre/info "stopping server")
          (stop server)))
