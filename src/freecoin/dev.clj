;; Freecoin - digital social currency toolkit

;; part of Decentralized Citizen Engagement Technologies (D-CENT)
;; R&D funded by the European Commission (FP7/CAPS 610349)

;; Copyright (C) 2015 Dyne.org foundation
;; Copyright (C) 2015 Thoughtworks, Inc.

;; Sourcecode designed, written and maintained by
;; Denis Roio <jaromil@dyne.org>

;; With contributions by
;; Arjan Scherpenisse <arjan@scherpenisse.net>

;; This program is free software: you can redistribute it and/or modify
;; it under the terms of the GNU Affero General Public License as published by
;; the Free Software Foundation, either version 3 of the License, or
;; (at your option) any later version.

;; This program is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;; GNU Affero General Public License for more details.

;; You should have received a copy of the GNU Affero General Public License
;; along with this program.  If not, see <http://www.gnu.org/licenses/>.

(ns freecoin.dev
  (:require [ns-tracker.core :refer [ns-tracker]]
            [clojure.java.shell :refer [sh]]))

(defn- notify [title message]
  (sh "notify-send" "-t" "500" title message))

(defn- check-namespace-changes [track]
  (try
    (doseq [ns-sym (track)]
      (notify "Reloading namespace" (str ns-sym))
      (require ns-sym :reload))
    (catch Throwable e (.printStackTrace e)))
  (Thread/sleep 500))

(defn start-nstracker []
  (notify "Tracker" "Starting change tracker")
  (let [track (ns-tracker ["src" "test"])]
    (doto
        (Thread.
         #(while true
            (check-namespace-changes track)))
      (.setDaemon true)
      (.start))))

