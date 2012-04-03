(ns circle.freeze-fn)

(defn resolve-var-str [s]
  (let [[_ ns v] (re-find #"#'([^/]+)/(.+)" s)]
    (require (symbol ns))
    (ns-resolve (symbol ns) (symbol v))))

(defn call* [pfn]
  (let [v (-> pfn :v resolve-var-str)]
    (apply v (-> pfn :args))))

(defrecord SerializableFn [v args]
  clojure.lang.IFn
  (invoke [this]
    (call* this))
  (call [this] ;; j.u.c.Concurrent
    (call* this))
  (run [this] ;; j.l.Runnable
    (call* this)))

(defn save
  "Takes a var and a seq of arguments. Returns an object that can
  be used to apply the fn with the args at a later point.

  The returned SerializableFn implements clojure.lang.IFn .invoke (with no
  arguments) and therefore, Runnable, Callable. All arguments must
  be serializable."  [v & args]
  (SerializableFn. (str v) args))
