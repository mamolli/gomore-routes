(ns routes.core-test
  (:require [clojure.test :refer :all]
            [routes.core :as r]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))

(def routes-1
  {:input ["C Gdansk Copenhagen 2010-03-04 12"
           "C Gdansk Warsaw 2010-03-04 32"]
   :search "S Gdansk Coppenhagen"
   :output "XX"})

(defn route-test [input output]
  ; we need to replay log
  (->> []
       (r/line->routes-db ["C Gdansk Coppenhagen 2010-03-04 12"])
       (r/line->routes-db ["C Gdansk Warsaw 2010-03-04 32"])))
  

(deftest line->routes-db-loop
  (line->routes []))