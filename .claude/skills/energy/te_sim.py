import math, sys, json

# Snapshot of MachineTier.kt — the skill must re-read that file each time and
# update these if they differ. TE = raw/1000; 20 tps.
TIERS = {
    'copper':    dict(hv=0.010, ini=100.0, rng=250.0, bonus=50.0,  cooling=0.99,   speed=0.5,  tr=1.5),
    'gold':      dict(hv=0.0125, ini=100.0, rng=300.0, bonus=100.0, cooling=0.994,  speed=0.75, tr=1.75),
    'steel':     dict(hv=0.015, ini=100.0, rng=400.0, bonus=100.0, cooling=0.996,  speed=1.0,  tr=2.0),
    'netherite': dict(hv=0.0175, ini=100.0, rng=500.0, bonus=100.0, cooling=0.9975, speed=1.25, tr=2.25),
}
# Vanilla furnace burn times (FuelValues.vanillaBurnTimes, baseUnit 200) and the
# generator's fuel value (2 = to_stars:heat_generator_2x, 0 = c:buckets, else 1).
FUELS = {
    'coal': dict(dur=1600, val=2), 'charcoal': dict(dur=1600, val=2),
    'coal_block': dict(dur=16000, val=2), 'blaze_rod': dict(dur=2400, val=1),
    'planks': dict(dur=300, val=1), 'log': dict(dur=300, val=1),
    'slab': dict(dur=150, val=1), 'stick': dict(dur=100, val=1),
    'wool': dict(dur=100, val=1), 'dried_kelp_block': dict(dur=4001, val=1),
    'lava_bucket': dict(dur=20000, val=0),
}

class Gen:
    """One heat generator with `qty` fuel items burned contiguously, then cooling."""
    def __init__(self, tier, fuel, qty):
        g = TIERS[tier]; f = FUELS[fuel]
        self.hv, self.ini, self.rng, self.bonus, self.cooling, self.speed = g['hv'], g['ini'], g['rng'], g['bonus'], g['cooling'], g['speed']
        self.per = math.ceil(f['dur'] * self.speed)
        self.add = f['val'] * self.hv
        self.dur = 0
        self.left = qty
        self.heat = 0.0
    def tick(self):
        if self.dur > 0:
            self.heat = max(self.heat, self.ini) + self.add
            self.dur -= 1
        else:
            if self.left > 0:
                self.left -= 1
                self.dur = self.per
            else:
                self.heat = min(self.heat, self.ini + self.rng) * self.cooling
                if self.heat < self.ini:
                    self.heat = 0.0
        if self.heat >= self.ini + self.rng:
            self.heat = self.ini + self.rng + self.bonus
    @property
    def done(self):
        return self.dur <= 0 and self.left == 0 and self.heat == 0.0

def run_network(generators, turbines, fill=3, ticks_after=10000000):
    """One heat-pipe network: the given generators, piped to the given boilers.

    `turbines` is a list of tier names, one per boiler, IN THE ORDER THE NETWORK
    VISITS THEM (HeatBlockNetworkType fills boilers in this order, so earlier
    entries are the ones that get heat first). Each boiler has one rotor and one
    turbine of that tier, so a mixed-tier network is modelled by listing the tiers
    in priority order. Returns network totals plus per-turbine breakdown.
    """
    n = len(turbines)
    total = 0
    per_total = [0] * n
    per_peak = [0.0] * n
    first = None
    peak = 0
    peak_tick = None
    active_max = 0
    t = 0
    while t < ticks_after:
        for g in generators:
            g.tick()
        tot = sum(g.heat for g in generators)
        active = max(0, min(int(tot / 100.0), n))
        active_max = max(active_max, active)
        share = (tot / active) if active > 0 else 0.0
        e_tot = 0
        for i, tier in enumerate(turbines):
            H = min(max(share if i < active else 0.0, 0.0), 200.0)
            e = int((H / 100.0) * (fill / 3.0) * 1000 * TIERS[tier]['tr'])  # .toLong()
            per_total[i] += e
            if e > per_peak[i]:
                per_peak[i] = e
            e_tot += e
        if e_tot > 0 and first is None:
            first = e_tot
        if e_tot > peak:
            peak = e_tot
            peak_tick = t
        total += e_tot
        if all(g.done for g in generators):
            break
        t += 1
    return {
        'first_te_t': round(first / 1000, 3) if first else 0,
        'peak_te_t': round(peak / 1000, 3),
        'ramp_s': round((peak_tick or 0) / 20.0, 2),
        'total_te': round(total / 1000, 1),
        'sim_s': round((t + 1) / 20.0, 1),
        'max_active_boilers': active_max,
        'per_turbine': [
            {'tier': turbines[i], 'total_te': round(per_total[i] / 1000, 1), 'peak_te_t': round(per_peak[i] / 1000, 3)}
            for i in range(n)
        ],
    }

def main():
    spec = json.loads(sys.argv[1])
    if 'networks' in spec:
        out = []
        for net in spec['networks']:
            gens = [Gen(g['tier'], g['fuel'], g['qty']) for g in net['generators']]
            turs = [t['tier'] for t in net['turbines']]
            out.append(run_network(gens, turs, net.get('fill', 3)))
        print(json.dumps(out, indent=1))
    else:
        # Shorthand: one generator + one turbine. "stacks" multiplies independent copies.
        r = run_network([Gen(spec['tier'], spec['fuel'], spec['qty'])], [spec['tier']], spec.get('fill', 3))
        n = spec.get('stacks', 1)
        if n > 1:
            for k in ('first_te_t', 'peak_te_t', 'total_te'):
                r[k] = round(r[k] * n, 3)
            r['stacks'] = n
        print(json.dumps(r))

if __name__ == '__main__':
    main()
