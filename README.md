[![CircleCI](https://circleci.com/gh/beetleman/shadow-cljs-hooks.svg?style=svg)](https://circleci.com/gh/beetleman/shadow-cljs-hooks)
[![Clojars Project](https://img.shields.io/clojars/v/org.clojars.beetleman/shadow-cljs-hooks.svg)](https://clojars.org/org.clojars.beetleman/shadow-cljs-hooks)

# shadow-cljs-hooks

This library provides collections of useful hooks for [shadow-cljs]:

- `shadow-cljs-hooks.index` - generating `index.html`
- `shadow-cljs-hooks.fulcro-css` - generating CSS file for [Fulcro3]

## Getting Started

Add the following dependency to your `deps.edn` file:

``` clojure
org.clojars.beetleman/shadow-cljs-hooks {:mvn/version "current version from clojars"}
```

Add some [build hook] to `shadow-cljs.edn`, eg. `shadow-cljs-hooks.index/hook`:

``` clojure
{...
 :builds
 {:app {:target ...
        :build-hooks
        [(shadow-cljs-hooks.index/hook)]
        ...}}}}
```

checkout [fulcro3 example] to seeing it in action.

## Hooks

### `shadow-cljs-hooks.index`

TODO

### `shadow-cljs-hooks.fulcro-css`

TODO

[shadow-cljs]: https://github.com/thheller/shadow-cljs
[fulcro3]: https://github.com/fulcrologic/fulcro
[garden]: https://github.com/noprompt/garden
[build hook]: https://shadow-cljs.github.io/docs/UsersGuide.html#build-hooks
[fulcro3 example]: ./example
