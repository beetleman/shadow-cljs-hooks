(ns shadow-cljs-hooks.spec
  (:require [expound.alpha :as expound]
            [taoensso.timbre :as log]
            [clojure.spec.alpha :as s]))


(s/def ::output-dir (s/and string? (complement empty?)))
(s/def ::asset-path (s/and string? (complement empty?)))
(s/def :shadow.build/config (s/keys :req-un [::output-dir ::asset-path]))
(s/def ::build-state (s/keys :req [:shadow.build/config]))


(defn valid? [spec x]
  (let [valid (s/valid? spec x)]
    (if-not valid
      (log/error (expound/expound-str spec x)))
    valid))
