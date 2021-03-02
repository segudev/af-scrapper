(ns af-back.parsing
  (:require [hickory.core :refer :all]
            [hickory.select :as s]
            [clj-http.client :as client]
            [clojure.string :as string])
  (:gen-class))

(def URL "https://fr.audiofanzine.com/petites-annonces/acheter/")

(def af-selling-main-page
  (-> (client/get URL {:insecure? true})
      :body
      parse
      as-hickory))

(defn extract-annonces [page]
  (s/select (s/class "clearfix") page))

(defn handle-error [e s]
  (println "Price parsing exception for " s)
  (println e)
  nil)

(defn sanitize-price [s]
  (try (-> s
           (string/replace #"[  €]" "")
           (string/replace #"," ".")
           (Float/parseFloat))
       (catch Exception error
         (handle-error error s))))

(defn id [annonce]
  (:id (:attrs annonce)))

(defn img [annonce]
  (-> (s/select (s/class "playlist-row-thumbnail")
                annonce)
      first :content first :attrs))

(defn title [annonce]
  (-> (s/select (s/class "playlist-row-title")
                annonce)
      first :content first))

(defn rel-url [annonce]
  (-> (s/select (s/class "playlist-row-content")
                annonce)
      first :content second :attrs :href))

(defn price [annonce]
  (-> (s/select (s/class "playlist-price")
                annonce)
      first :content first sanitize-price))

(defn timeplace [annonce]
  (-> (s/select (s/class "playlist-row-meta")
                annonce)
      first :content first string/trim))

;;If we need to split time from location ->
;;(defn time-split [s]
;;  (let [[time place] (str/split s #" - ")]
;;    conj {:time time :place place}))

(defn summary [annonce]
  (-> (s/select (s/class "main-text")
                annonce)
      first :content))

(defn parse-annonce [annonce]
  (zipmap [:id :img :title :rel-url :price :timeplace :summary]
          ((juxt id img title rel-url price timeplace summary) annonce)))

(defn current-annonces []
  (map #(parse-annonce %)
       (extract-annonces af-selling-main-page)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;(def now (.atZone (java.time.Instant/now ) (java.time.ZoneId/of "Europe/Paris")))

;;(def testing (extract-annonces af-selling-main-page))
;;(map #(id %) testing)
;;(map #(img %) testing)
;;(map #(title %) testing)
;;(map #(rel-url %) testing)
;;(map #(price %) testing)