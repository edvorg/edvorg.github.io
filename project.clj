(defproject edvorg "0.3.2-SNAPSHOT"
  :description "simple dictionary web-app"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :repositories [["jitpack" "https://jitpack.io"]
                 ["clojars" "https://clojars.org/repo"]
                 ;; ["central" "https://repo1.maven.org/maven2"]
                 ;; ["clojure" "https://build.clojure.org/releases"]
                 ;; ["java.net" "https://download.java.net/maven/2"]
                 ;; ["jboss.release" "https://repository.jboss.org/nexus/content/groups/public"]
                 ;; ["terracotta-releases" "https://www.terracotta.org/download/reflector/releases"]
                 ;; ["terracotta-snapshots" "https://www.terracotta.org/download/reflector/snapshots"]
                 ;; ["apache.snapshots" "https://repository.apache.org/snapshots"]
                 ]

  :dependencies [[org.clojure/clojure "1.9.0"] ;; server side language
                 [org.clojure/clojurescript "1.10.64" :scope "provided"] ; client side language
                 [rocks.clj/configuron "0.1.1-SNAPSHOT"] ;; configuration
                 [rocks.clj/lenses "0.1.0-SNAPSHOT"] ;; lenses
                 [rocks.clj/transit "0.1.0-SNAPSHOT"] ;; transit support
                 [rocks.clj/reagent-handler "0.1.0-SNAPSHOT"] ;; reagent server-side
                 [com.taoensso/timbre "4.10.0"] ;; logging
                 [org.clojure/core.async "0.4.474"] ;; concurrency/message processing
                 [mount "0.1.12"] ;; state handling
                 [reagent "0.7.0" :exclusions [com.google.guava/guava]] ;; client side rendering
                 [ring/ring "1.6.3"] ;; http server
                 [ring/ring-defaults "0.3.1"] ;; http server defaults
                 [org.clojars.cvillecsteele/ring-permacookie-middleware "1.4.0"] ;; identify visitors
                 [ring/ring-mock "0.3.2"] ;; http server mocking
                 [org.immutant/immutant "2.1.10"];; http server
                 [compojure "1.6.0"] ;; server side routing
                 [secretary "1.2.3"] ;; client side routing
                 [venantius/accountant "0.2.4" :exclusions [org.clojure/tools.reader]] ;; client side routing config
                 [cheshire "5.8.0"] ;; json support
                 [hiccup "1.0.5"] ;; server side rendering
                 [jarohen/chord "0.8.1" :exclusions [http-kit]] ;; web sockets support
                 [clj-http "3.7.0"] ;; server side http client
                 [cljs-http "0.1.44"] ;; client side http client
                 [figwheel-sidecar "0.5.15" :exclusions [com.google.guava/guava]] ;; figwheel
                 ]

  :plugins [[lein-environ "1.1.0"]
            [lein-cljsbuild "1.1.7"]
            [lein-asset-minifier "0.4.4" :exclusions [org.clojure/clojure]]
            [lein-figwheel "0.5.15"]
            [rocks.clj/lein-give-me-my-css "0.1.0-SNAPSHOT"]]

  :ring {:handler edvorg.handler/dev-routes
         :init edvorg.core/-main
         :port 3449
         :uberwar-name "edvorg.war"}

  :min-lein-version "2.5.0"

  :uberjar-name "edvorg.jar"

  :main edvorg.core

  :clean-targets ^{:protect false} [:target-path
                                    [:cljsbuild :builds :app :compiler :output-dir]
                                    [:cljsbuild :builds :app :compiler :output-to]]

  :test-paths ["test/clj" "test/cljs" "test/cljc"]
  :source-paths ["src/clj" "src/cljc"]
  :resource-paths ["resources"]

  :minify-assets [[:css {:source "resources/public/css/site.css" :target "resources/public/css/site.min.css"}]]

  :cljsbuild {:builds {:min {:source-paths ["src/cljs" "src/cljc"]
                             :compiler {:main "edvorg.core"
                                        :asset-path "js/out"
                                        :output-to "resources/public/js/app.js"
                                        :output-dir "target/uberjar"
                                        :optimizations :advanced
                                        :pretty-print  false}}
                       :app {:source-paths ["src/cljs" "src/cljc"]
                             :figwheel {:on-jsload "edvorg.core/mount-root"}
                             :compiler {:main "edvorg.core"
                                        :asset-path "js/out"
                                        :output-to "resources/public/js/app.js"
                                        :output-dir "resources/public/js/out"
                                        :source-map true
                                        :optimizations :none
                                        :pretty-print true}}}}

  :figwheel {:http-server-root "public"
             :server-port 3449
             :nrepl-port 7002
             :nrepl-middleware ["cider.nrepl/cider-middleware"
                                "refactor-nrepl.middleware/wrap-refactor"
                                "cemerick.piggieback/wrap-cljs-repl"]
             :css-dirs ["resources/public/css"]
             :ring-handler edvorg.handler/dev-routes
             :server-logfile false}

  :profiles {:dev {:repl-options {:init-ns edvorg.core
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

                   :dependencies [[org.clojure/tools.nrepl "0.2.13"]
                                  [com.cemerick/piggieback "0.2.2"]]

                   :plugins [[cider/cider-nrepl "0.17.0-SNAPSHOT"]
                             [org.clojure/tools.namespace "0.3.0-alpha2"
                              :exclusions [org.clojure/tools.reader]]
                             [refactor-nrepl "2.4.0-SNAPSHOT"
                              :exclusions [org.clojure/clojure]]]

                   :env {:mode :dev
                         :client-config-keys [[:mode]
                                              [:window-size]
                                              [:tile-asset-size]
                                              [:tile-ui-size]]
                         :port 12309
                         :user-token-key "edvorg_token"
                         :window-size [800 800]
                         :tile-asset-size [16 16]
                         :tile-ui-size [32 32]}}

             :uberjar {:hooks [minify-assets.plugin/hooks]
                       :prep-tasks ["compile" ["cljsbuild" "once" "min"]]
                       :env {:mode :uberjar}
                       :aot :all
                       :omit-source true}}

  :jvm-opts ["-Xms256M"
             "-Xmx1024M"])
