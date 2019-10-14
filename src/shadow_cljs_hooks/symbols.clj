(ns shadow-cljs-hooks.symbols)

(defn eval-symbol [sym]
  (require (symbol (namespace sym)) :reload)
  (eval sym))
