/**
 * Demo 16 — Multi-Agent Pull Request Quality Gate
 * ===============================================
 * This file is the IMPLEMENTATION submitted in a pull request. It is the target
 * of a three-agent, read-only quality gate:
 *
 *   pr-quality-gate  (coordinator)
 *     ├── requirements-analyst    -> extracts testable acceptance criteria
 *     └── implementation-reviewer -> maps code + tests to criteria, runs tests
 *
 * The PR adds promo-code redemption at checkout. It runs and the existing tests
 * pass — but it contains TWO INTENTIONAL defects the quality gate should catch by
 * comparing the code against the acceptance criteria in PR.md:
 *
 *   * AC-04 (case-insensitive matching): the code matches promo codes with a
 *     case-sensitive comparison, so "save10" fails to match "SAVE10".
 *   * AC-05 (minimum-subtotal boundary): the code uses ">" instead of ">=", so a
 *     promo is wrongly rejected at exactly its minimumSubtotal.
 *
 * There is also a robustness finding: an unknown code makes `find()` return
 * undefined, then reading a property throws an opaque TypeError instead of a
 * clear "promo not found" error.
 *
 * Demo flow (see the language README for the full script):
 *   1. Put this folder's changes on a feature branch and open a Pull Request
 *   2. Install the three agents into .github/agents/
 *   3. Run the `pr-quality-gate` agent against the PR
 *   4. Read the evidence-based decision (expected: BLOCK)
 */

class PromoService {
  /**
   * @param {{ code: string, value: number, isPercentage: boolean, minimumSubtotal: number, expiresOn: Date }[]} promos
   * @param {Date} today
   */
  constructor(promos, today) {
    this.promos = [...promos];
    this.today = today;
  }

  /**
   * Applies the promo identified by `code` to `subtotal` and returns the
   * discounted total.
   * @param {string} code
   * @param {number} subtotal
   * @returns {number}
   */
  apply(code, subtotal) {
    // Robustness gap: no not-found check. An unknown code (or the right code in
    // the wrong case) makes `find` return undefined, so the next line throws an
    // opaque TypeError instead of a clear "promo not found" error.
    // AC-04 defect: case-sensitive comparison — "save10" !== "SAVE10".
    const promo = this.promos.find((p) => p.code === code);

    // Reject expired promos.
    if (promo.expiresOn < this.today) {
      throw new Error(`Promo ${code} expired on ${promo.expiresOn.toISOString().slice(0, 10)}.`);
    }

    // AC-05 defect: should be ">=". At exactly the minimum subtotal the promo is
    // wrongly rejected and the customer pays full price.
    if (subtotal > promo.minimumSubtotal) {
      const discount = promo.isPercentage ? (subtotal * promo.value) / 100 : promo.value;
      const total = subtotal - discount;
      return total < 0 ? 0 : total; // Total never goes below zero.
    }

    return subtotal;
  }
}

function main() {
  const promos = [
    { code: 'SAVE10', value: 10, isPercentage: true, minimumSubtotal: 50, expiresOn: new Date('2030-12-31') },
    { code: 'WELCOME', value: 5, isPercentage: false, minimumSubtotal: 0, expiresOn: new Date('2030-12-31') },
  ];

  const service = new PromoService(promos, new Date('2026-07-27'));

  console.log(`SAVE10 on 150.00 -> ${service.apply('SAVE10', 150).toFixed(2)}`); // 135.00 (correct)
  console.log(`WELCOME on 20.00 -> ${service.apply('WELCOME', 20).toFixed(2)}`); // 15.00  (correct)

  console.log();
  console.log('The quality gate should flag these two cases against PR.md:');
  console.log("  service.apply('save10', 150)  // AC-04: throws - matching is case-sensitive");
  console.log("  service.apply('SAVE10', 50)   // AC-05: returns 50.00 - off-by-one at the minimum");
}

if (require.main === module) {
  main();
}

module.exports = { PromoService };
