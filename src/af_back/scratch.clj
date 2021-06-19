(ns af-back.scratch)


(filter odd? '(1 2 3 4 5 6 7 8 9 10))

(defn filter-reducer [f coll]
  (reduce conj (f coll)))

(filter-reducer odd? '(1 2 3 4 5 6 7 8 9 10))


(use 'af-back.parsing)

(eval af-back.parsing/af-selling-main-page)
 