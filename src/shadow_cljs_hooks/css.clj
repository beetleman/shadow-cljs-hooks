(ns shadow-cljs-hooks.css
  (:require [taoensso.timbre :as log]
            [clojure.spec.alpha :as s]))

(def css-path-key ::css-path)

(defn get-path [build-state]
  (get build-state css-path-key))

(defn assoc-path [build-state path]
  (assoc build-state css-path-key path))

(defn write! [css output-dir file-name]
  (try
    (when-not (.exists output-dir)
      (.mkdirs output-dir))
    (spit (str output-dir "/" file-name) css)
    (log/info "css saved")
    (catch Exception e
      (log/error "css save failed" e))))


(s/def :garden/pretty-print? boolean?)
(s/def :garden/output-to nil?) ; protect user form set it
(s/def ::garden-flags (s/keys :opt-un [:garden/pretty-print? :garden/output-to]))
