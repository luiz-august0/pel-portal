# Styling

## Tailwind is the only styling layer

- All styling is done with Tailwind utility classes.
- Global tokens (colors, fonts, custom variants) live in `src/app/globals.css`.
- Don't introduce CSS modules, styled-components, emotion (beyond what's already pulled by deps), or inline `<style>` blocks.

## `cn()` helper

- Always merge classes with `cn()` (clsx + tailwind-merge) from `@/helpers/cn`. This avoids conflicting utilities silently overriding each other.
  ```tsx
  <div className={cn("p-4 bg-white rounded-lg", isActive && "ring-2 ring-primary", className)} />
  ```
- Customized components must accept and forward a `className` prop merged via `cn()`.

## Design tokens

- Use semantic Tailwind tokens (`bg-background`, `text-foreground`, `text-muted-foreground`, `border-destructive`, `bg-primary`) instead of raw colors when the project's theme defines them.
- Stick to the existing theme palette before reaching for arbitrary values like `bg-[#abcdef]`.

## Responsive & dark mode

- Mobile-first: write base classes for mobile, layer `sm:`, `md:`, `lg:` upward.
- Dark-mode classes use the `dark:` variant — only add when a dark theme is supported.

## Icons

- Use **lucide-react** (shadcn default). Reuse existing icons from feature code instead of importing from a different icon library.

## Don't

- ❌ Don't write CSS files outside of `globals.css` and the shadcn-generated ones.
- ❌ Don't combine arbitrary `style={{}}` props with Tailwind classes for layout — pick one.
- ❌ Don't inline raw hex colors when a theme token exists.
