(ns high-voltage.subs
  (:require [re-frame.core :as r :refer [reg-sub]]))

(reg-sub
 :get-greeting
 (fn [db _]
   (:greeting db)))

(reg-sub
  :query-db
  (fn [db [_ path]]
    (if (sequential? path)
      (get-in db path)
      (get db path))))
