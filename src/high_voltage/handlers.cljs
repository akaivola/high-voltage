(ns high-voltage.handlers
  (:require
    [re-frame.core :refer [reg-event-db ->interceptor]]
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

(r/reg-event-db
  :set-db
  (fn [db [_  path value]]
    (assoc-in db
              (if (keyword? path)
                [path]
                path)
              value)))

(r/reg-event-db
  :state-update
  (fn [db [_ f]]
    (or (f db)
        db)))
