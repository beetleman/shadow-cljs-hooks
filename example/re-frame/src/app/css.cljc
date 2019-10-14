(ns app.css)

(defn ->class [s]
  (keyword (str "." s)))

(def count-button "count-button")
(def root "root")

(def css
  [[(->class root) {:margin "30px"}
    [(->class count-button) {:background-color "#4CAF50"
                             :color            :white
                             :padding          "5px 20px"
                             :text-align       :center
                             :text-decoration  :none
                             :font-size        "20px"}]]])
