(ns hot2pat.core
  (:require [clojure.java.io :as io])
  (:import (java.nio.file Paths)
           (java.util Random))
  (:gen-class))

(def fmt-string (str "<instance objName=\"%s\" x=\"%d\" y=\"%d\" name=\"%s\" locked=\"0\" "
                     "code=\"\" scaleX=\"1\" scaleY=\"1\" colour=\"4294967295\" rotation=\"%f\"/>"))

(def tab (read-string (slurp (io/resource "objtab.edn"))))


(let [prng (Random.)]
  (defn generate-name
    ([pref] (str pref "_" (.toUpperCase (Integer/toHexString (.nextInt prng)))))
    ([] (generate-name "inst"))))


(defn parse-obj [obj]
  (->> obj
       slurp
       clojure.string/split-lines
       (partition 7)
       (map (partial into []))
       (map (fn [v] {:obj-name (tab (Integer/parseInt (v 5)))
                     :x (Integer/parseInt (v 1))
                     :y (Integer/parseInt (v 2))
                     :name (generate-name "obj")
                     :rot (Double/parseDouble (v 4))}))))


(defn parse-wll [wll]
  (->> wll
       slurp
       clojure.string/split-lines
       (map #(Integer/parseInt %))
       (partition 5)
       (map (partial into []))
       (map (fn [v] {:obj-name (tab (v 0))
                     :x (v 1)
                     :y (v 2)
                     :name (generate-name "wall")
                     :rot 0.0}))))


(defn handle-level [obj wll out]
  (when (and
          (.exists obj)
          (.exists wll))
    (let [insts (concat (parse-obj obj) (parse-wll wll))]
      (spit out (clojure.string/join "\n" (map #(format
                       fmt-string
                       (:obj-name %)
                       (:x %)
                       (:y %)
                       (:name %)
                       (:rot %)) insts)))
      (println "Processed " (.getAbsolutePath out))
      true)))


(defn handle-dir [dir]
  (let [root (Paths/get dir (into-array String []))
        res #(.toFile (.resolve root (format "level%d.%s" %2 %1)))
        objs (map (partial res "obj") (range))
        wlls (map (partial res "wll") (range))
        outs (map (partial res "xml") (range))
        pairs (map vector objs wlls outs)]

    (every?
      (fn [[obj wll out]] (handle-level obj wll out))
      pairs)))


(defn -main [& dirs]
  (doall (map handle-dir dirs)))