from pathlib import Path
root=Path(__file__).resolve().parents[1]
frontend=root/'frontend'; css=(frontend/'css'/'styles.css').read_text(encoding='utf-8')
errors=[]
for path in sorted(frontend.glob('*.html')):
    text=path.read_text(encoding='utf-8')
    if 'experience-page' not in text: errors.append(f'{path.name}: missing experience-page')
    if 'js/motion.js' not in text: errors.append(f'{path.name}: missing motion runtime')
for token in ['--paper:','--ink:','--acid:','prefers-reduced-motion','.experience-shell','.knowledge-object']:
    if token not in css: errors.append(f'styles.css: missing {token}')
if errors: raise SystemExit('\n'.join(errors))
print('REFERENCE UI CHECK: PASS')
