from pathlib import Path
root=Path(__file__).resolve().parents[1]/'frontend'
required=['index.html','explore.html','search.html','resource.html','algorithm-lab.html','analytics.html','admin.html','css/styles.css','js/api.js']
missing=[x for x in required if not (root/x).exists()]
assert not missing, f'missing frontend files: {missing}'
print('Frontend contract checks passed')
