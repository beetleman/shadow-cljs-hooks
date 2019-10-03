(ns shadow-cljs-hooks.spec
  (:require [expound.alpha :as expound]
            [taoensso.timbre :as log]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.alpha :as s]))

(s/def ::not-empty-string (s/and string? (complement empty?)))

(defn file-name-generator [file-ext]
  (gen/fmap #(str % "." file-ext)
            (gen/such-that (complement empty?)
                           (gen/string-alphanumeric))))

(defn file-name-spec [file-ext]

  (s/with-gen (s/and ::not-empty-string
                     #(re-matches (re-pattern (str ".+\\." file-ext "$"))  %))
    #(file-name-generator file-ext)))


(s/def ::output-dir ::not-empty-string)
(s/def ::asset-path ::not-empty-string)
(s/def :shadow.build/config (s/keys :req-un [::output-dir ::asset-path]))
(s/def ::build-state (s/keys :req [:shadow.build/config]))

(defn valid? [spec x]
  (let [valid (s/valid? spec x)]
    (if-not valid
      (log/error (expound/expound-str spec x)))
    valid))
