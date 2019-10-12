(ns app.css)

(defn str->class [s]
  (keyword (str "." s)))

(def count-button "count-button")
(def root "root")

(def css
  [[(str->class root) {:margin "30px"}
    [(str->class count-button) {:background-color "#4CAF50"
                                :color            :white
                                :padding          "5px 20px"
                                :text-align       :center
                                :text-decoration  :none
                                :font-size        "20px"}]]])
