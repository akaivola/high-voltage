(ns high-voltage.handlers
  (:require
    [re-frame.core :as r :refer [reg-event-db ->interceptor]]
    [high-voltage.db :as db :refer [app-db]]))

(reg-event-db
  :initialize-db
  (fn [_ _]
    app-db))

(reg-event-db
  :init-complete
  (fn [db _]
    (assoc db :init-complete? true)))

(reg-event-db
  :set-greeting
  (fn [db [_ value]]
    (assoc db :greeting value)))

(reg-event-db
  :set-db
  (fn [db [_  path value]]
    (assoc-in db
              (if (keyword? path)
                [path]
                path)
              value)))

(reg-event-db
  :update-db
  (fn [db [_ path f]]
    (update-in db
               (if (keyword? path)
                 [path]
                 path)
               f)))

(reg-event-db
  :update-f
  (fn [db [_ f]]
    (or (f db)
        db)))
