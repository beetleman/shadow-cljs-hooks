(ns shadow-cljs-hooks.css)

(def css-path-key ::css-path)

(defn get-path [build-state]
  (get build-state css-path-key))

(defn assoc-path [build-state path]
  (assoc build-state css-path-key path))
