(ns af-back.core
  (:require [af-back.parsing :refer :all]
            [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [ring.util.response :refer :all]
            [clojure.data.json :as json])
  (:gen-class))

(defn scrapping-result [page]
  {:status  200
   :headers {"Content-Type" "application/json"}
   :body    (json/write-str (current-annonces (Integer/parseInt page)))})

(defroutes app-routes
  (GET "/" [] (slurp "resources/public/home.html"))
  (GET "/scrap/:page" [page] (scrapping-result page))
  (route/not-found "Error, page not found!"))

(defn -main
  "This is our main entry point"
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    ; Run the server with Ring.defaults middleware
    (server/run-server (wrap-defaults #'app-routes site-defaults)
                       {:port port})
    ; Run the server without ring defaults
    ;(server/run-server #'app-routes {:port port})
    (println (str "Running webserver at http://127.0.0.1:" port "/"))))


