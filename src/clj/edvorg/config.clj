(ns edvorg.config
  (:require [config.core :as config]
            [taoensso.timbre :as timbre]))

(defn get-project-env [mode]
  (try ;; allows to reload environment configuration in dev mode
    (let [project (-> (System/getProperty "user.dir")
                      (str "/project.clj")
                      (slurp)
                      (read-string))
          profiles (->> project
                        (drop-while #(not (= :profiles %)))
                        (drop 1)
                        (first))
          figwheel (->> project
                        (drop-while #(not (= :figwheel %)))
                        (drop 1)
                        (first))
          env (-> (get-in profiles [mode :env])
                  (dissoc :mode)
                  (assoc :figwheel figwheel))]
      env)
    (catch Throwable e
      {})))

(def env (let [{:keys [mode]} config/env]
           (case mode
             :dev (proxy [clojure.lang.IDeref] []
                    (deref []
                      (->> (get-project-env mode)
                           (merge config/env))))
             :uberjar (proxy [clojure.lang.IDeref] []
                        (deref []
                          config/env)))))
