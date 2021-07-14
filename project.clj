(defproject af-back "0.1.0-SNAPSHOT"
  :description "AF synths parser"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.11.0-alpha1"]
                 [hickory "0.7.1"]
                 [compojure "1.6.2"]
                 [ring/ring-defaults "0.3.2"]
                 [org.clojure/data.json "0.2.6"]
                 [http-kit "2.5.3"]
                 [jumblerg/ring-cors "2.0.0"]]
  :plugins [[io.taylorwood/lein-native-image "0.3.1"]]
  :target-path "target/%s"
  :native-image {:graal-bin "/Library/Java/JavaVirtualMachines/graalvm-ce-java11-21.1.0/Contents/Home/bin"
                 :jvm-opts ["-Dclojure.compiler.direct-linking=true"]
                 :opts ["--enable-url-protocols=http, https "
                        "--report-unsupported-elements-at-runtime" ;; ignore native-image build errors
                        "--initialize-at-build-time"
                        "--no-server" ;; TODO issue with subsequent builds failing on same server
                        "--verbose"]
                 :name "af-scrap"}
  :main af-back.core
;  :uberjar-name "af-back-standalone.jar"
  :profiles {:dev {:dependencies [[org.clojure/test.check "0.9.0"]]}
               :uberjar {:aot :all}})