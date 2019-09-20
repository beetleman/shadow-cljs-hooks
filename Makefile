.PHONY: test

test:
	clojure -A:test -m kaocha.runner
