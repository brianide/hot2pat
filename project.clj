(defproject hot2pat "0.2.0-SNAPSHOT"
  :description "Utility for converting Hotline Miami 2 editor levels to GMS rooms"
  :url "https://github.com/brianide/hot2pat"
  :license {:name "Mozilla Public License Version 2.0"
            :url "https://www.mozilla.org/en-US/MPL/2.0/"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.xml "0.0.8"]]
  :main hot2pat.core
  :profiles {:uberjar {:aot :all}})
