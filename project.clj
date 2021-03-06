(defproject af-back "0.1.0-SNAPSHOT"
  :description "AF synths parser"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [clj-http "3.12.0"]
                 [hickory "0.7.1"]
                 [compojure "1.6.2"]
                 [ring/ring-defaults "0.3.2"]
                 [org.clojure/data.json "0.2.6"]
                 [http-kit "2.3.0"]
                 [jumblerg/ring-cors "2.0.0"]]
  :main ^:skip-aot af-back.core
  :uberjar-name "af-back-standalone.jar"
  :profiles {:uberjar {:aot :all}})
