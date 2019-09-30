(ns shadow-cljs-hooks.fulcro-styles)

(defonce tracker (atom 0))

(defmacro foo []
  (swap! tracker inc))
