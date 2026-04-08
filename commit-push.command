#!/bin/bash
# ============================================================
# commit-push.command — Commit & push all staged changes
# Double-click this file in Finder to run it.
# ============================================================

cd "$(dirname "$0")"
echo "📁 Working in: $(pwd)"
echo ""

# Remove stale git lock if it exists
if [ -f ".git/index.lock" ]; then
  echo "🔓 Removing stale git lock..."
  rm -f ".git/index.lock" && echo "   ✅ Lock removed." || echo "   ⚠️  Could not remove lock."
fi

# Stage all changes
echo ""
echo "📦 Staging all changes..."
git add -A
echo "   ✅ Done."

# Commit
echo ""
echo "💾 Committing..."
git commit -m "feat: complete MVP backend — Spring Boot + PostgreSQL working

- Fix startup: sql.init.mode=always + ddl-auto=none
- Remove hypersistence-utils (PostgreSQLEnumType not available in Hibernate 6)
- Use standard JPA @Enumerated(EnumType.STRING) with VARCHAR columns
- Add idempotent schema.sql and data.sql (IF NOT EXISTS / ON CONFLICT)
- Add controllers, services, DTOs, Thymeleaf templates
- Add run-app.command helper script
- Add AGENTS.md and project documentation"

echo "   ✅ Committed."

# Push
echo ""
echo "🚀 Pushing to GitHub..."
git push origin main
echo ""
echo "✅ All done! Check GitHub to confirm."
echo ""
read -p "Press Enter to close..."
