(ns high-voltage.obdii
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as r]
   [promesa.core :as p]
   [reagent.ratom :as ratom]
   [promesa.async-cljs :refer-macros [async]]
   [oops.core :as o :refer [oget]]
   [cuerdas.core :as str]))

(def obd2 (js/require "react-native-obd2"))

(r/reg-sub-raw
  :bluetooth-device-name-list
  (fn [db _]
    (async
      (r/dispatch [:set-db :bluetooth/device-name-list]
                  (js->clj
                    (p/await (.getBluetoothDeviceNameList obd2))
                    :keywordize-keys true)))
    (ratom/make-reaction
      (fn [db]
        (:bluetooth/device-name-list @db)))))

(defn devices []

  )
