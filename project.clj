(defproject edvorg "0.3.2-SNAPSHOT"
  :description "simple dictionary web-app"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :repositories [["jitpack" "https://jitpack.io"]
                 ["central" "https://repo1.maven.org/maven2"]
                 ["clojure" "https://build.clojure.org/releases"]
                 ["clojars" "https://clojars.org/repo"]
                 ["java.net" "https://download.java.net/maven/2"]
                 ["jboss.release" "https://repository.jboss.org/nexus/content/groups/public"]
                 ["terracotta-releases" "https://www.terracotta.org/download/reflector/releases"]
                 ["terracotta-snapshots" "https://www.terracotta.org/download/reflector/snapshots"]
                 ["apache.snapshots" "https://repository.apache.org/snapshots"]]

  :dependencies [[org.clojure/clojure "1.8.0"] ;; server side language
                 [org.clojure/clojurescript "1.9.908" :scope "provided"] ; client side language
                 [clojure-future-spec "1.9.0-alpha14"] ;; spec
                 [com.taoensso/timbre "4.10.0"] ;; logging
                 [org.clojure/core.async "0.3.443"] ;; concurrency/message processing
                 [mount "0.1.11"] ;; state handling
                 [com.taoensso/carmine "2.16.0"] ;; redis client
                 [reagent "0.7.0"
                  :exclusions [com.google.guava/guava]] ;; client side rendering
                 [reagent-utils "0.2.1"] ;; client side utilities
                 [ring/ring-devel "1.6.2"] ;; http server extensions
                 [ring/ring-core "1.6.2"] ;; http server extensions
                 [ring/ring-defaults "0.3.1"] ;; http server extensions
                 [ring/ring-mock "0.3.1"] ;; http server extensions
                 [prone "1.1.4"] ;; http server extensions
                 [org.immutant/immutant "2.1.9"];; http server
                 [compojure "1.6.0"] ;; server side routing
                 [hiccup "1.0.5"] ;; server side rendering
                 [rocks.clj/configuron "0.1.0-SNAPSHOT"] ;; configuration
                 [jarohen/chord "0.8.1"
                  :exclusions [http-kit]] ;; web sockets support
                 [secretary "1.2.3"] ;; client side routing
                 [clj-http "3.7.0"] ;; server side http client
                 [cljs-http "0.1.43"] ;; client side http client
                 [cheshire "5.8.0"] ;; json support
                 [com.cognitect/transit-clj "0.8.300"] ;; serialization on server
                 [com.cognitect/transit-cljs "0.8.243"] ;; serialization on client
                 [clojure-csv/clojure-csv "2.0.2"] ;; csv support
                 [com.cemerick/url "0.1.1"] ;; url encoding support
                 [venantius/accountant "0.2.0"
                  :exclusions [org.clojure/tools.reader]] ;; client side routing configuration
                 [less-awful-ssl "1.0.1"] ;; ssl support
                 [org.bouncycastle/bcpkix-jdk15on "1.58"] ;; ssl support
                 [pandect "0.6.1"] ;; ssl support
                 [figwheel-sidecar "0.5.14"
                  :exclusions [com.google.guava/guava]] ;; figwheel
                 [org.clojars.cvillecsteele/ring-permacookie-middleware "1.4.0"] ;; identify visitors
                 [clojurewerkz/elastisch "3.0.0-beta2"
                  :exclusions [org.elasticsearch/elasticsearch]] ;; elasticsearch client
                 [org.elasticsearch/elasticsearch "2.4.6"
                  :exclusions [com.google.guava/guava]] ;; elasticsearch native client
                 ]

  :plugins [[lein-environ "1.1.0"]
            [lein-cljsbuild "1.1.7"]
            [lein-asset-minifier "0.4.3" :exclusions [org.clojure/clojure]]
            [lein-figwheel "0.5.14"]
            ]

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
                         :client-config-keys [[:mode]]
                         :port 12309
                         :redis {:host "127.0.0.1"
                                 :port 6379}
                         :elastic-hosts [["localhost" 9300]]
                         :user-token-key "edvorg_token"
                         :terms-per-query 10}}

             :uberjar {:hooks [minify-assets.plugin/hooks]
                       :prep-tasks ["compile" ["cljsbuild" "once" "min"]]
                       :env {:mode :uberjar
                             :client-config-keys [[:mode]]
                             :port 12309
                             :redis {:host "127.0.0.1"
                                     :port 6379}
                             :elastic-hosts [["localhost" 9300]]
                             :user-token-key "edvorg_token"
                             :terms-per-query 10}
                       :aot :all
                       :omit-source true}}
  :jvm-opts ["-Xms256M"
             "-Xmx1024M"])
