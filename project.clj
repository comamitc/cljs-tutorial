(defproject cljs-frontend "0.1.0-SNAPSHOT"
  :description "A ClojureScript Execise for after the clj-s-intro presentation"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src", "lib"]

  :dependencies [[org.clojure/clojure "1.8.0" :scope "provided"]
                 [org.clojure/clojurescript "1.9.229" :scope "provided"]
                 [org.clojure/core.async "0.2.395"]
                 [rum "0.10.7"]
                 [com.andrewmcveigh/cljs-time "0.4.0"]]

  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-figwheel "0.5.4-5"]
            [lein-pdo "0.1.1"]
            [lein-less "1.7.5"]
            [lein-asset-minifier "0.2.7" :exclusions [org.clojure/clojure]]]

  :min-lein-version "2.5.0"

  :clean-targets ^{:protect false}
  [:target-path
   [:cljsbuild :builds :app :compiler :output-dir]
   [:cljsbuild :builds :app :compiler :output-to]]

  :minify-assets {:assets
                   {"resources/public/css/site.min.css"
                    "resources/public/css/site.css"}}

  :figwheel {:css-dirs ["resources/public/css"]}

  :less {:source-paths ["src/less"]
         :target-path "resources/public/css"}

  :cljsbuild {:builds {:app
                       {:source-paths ["src", "lib"]
                        :figwheel  {:on-jsload "cljs-frontend.core/mount-root"}
                        :compiler
                        {:main cljs-frontend.core
                         :output-to "resources/public/js/app.js"
                         :output-dir "resources/public/js/out"
                         :asset-path   "js/out"
                         :source-map true
                         :optimizations :none
                         :pretty-print  true}}}}

  :aliases {"release" ["do" "clean" ["cljsbuild" "once" "release"]]
            "dev"     ["do" "clean" ["pdo" ["less" "auto"] ["figwheel" "app"]]]})
