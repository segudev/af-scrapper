(ns af-back.parsing
  (:require [hickory.core :refer :all]
            [hickory.select :as s]
            [clj-http.client :as client]
            [clojure.string :as string])
  (:gen-class))

(def URL "https://fr.audiofanzine.com/petites-annonces/acheter/")

(defn af-selling-main-page []
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
;;;;;;  /!\
(defn img [annonce]
  (-> (s/select (s/class "playlist-row-thumbnail")
                annonce)
      first :content first :content first :attrs :srcset))


(defn title [annonce]
  (-> (s/select (s/class "playlist-row-title")
                annonce)
      first :content first))

(defn rel-url [annonce]
  (-> (s/select (s/class "link-wrapper")
                annonce)
      first :attrs :href))


(defn price [annonce]
  (-> (s/select (s/class "playlist-price")
                annonce)
      first :content first sanitize-price))

(defn replace-and-trim [sentence char]
  (string/trim (string/replace sentence (str char " ") "")))

(defn hour [annonce]
  (-> (s/select (s/class "playlist-row-meta")
                annonce)
      first :content first :content last (replace-and-trim "à")))

(defn place [annonce]
  (-> (s/select (s/class "playlist-row-meta")
                annonce)
      first :content second :content first (replace-and-trim "-")))

(defn timeplace [annonce]
  (str (af-back.parsing/hour annonce) " - " (place annonce)))

(defn summary [annonce]
  (-> (s/select (s/class "main-text")
                annonce)
      first :content first))

(defn parse-annonce [annonce]
  (zipmap [:id :img :title :rel-url :price :timeplace :summary]
          ((juxt id img title rel-url price timeplace summary) annonce)))

(def current-annonces
  (map #(parse-annonce %)
       (extract-annonces (af-selling-main-page))))
       
(count current-annonces)

;next is toimplement paging
;so we can request the next 20 annonces


;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;(def now (.atZone (java.time.Instant/now ) (java.time.ZoneId/of "Europe/Paris")))

;;(def testing (extract-annonces af-selling-main-page))
;;(map #(id %) testing)
;;(map #(img %) testing)
;;(map #(title %) testing)
;;(map #(rel-url %) testing)
;;(map #(price %) testing)