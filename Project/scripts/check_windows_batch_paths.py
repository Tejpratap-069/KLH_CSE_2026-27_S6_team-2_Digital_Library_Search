from pathlib import Path
root=Path(__file__).resolve().parents[1]
for name in ['compile.bat','test.bat']:
    text=(root/'scripts'/name).read_text(encoding='utf-8',errors='replace')
    assert 'EnableDelayedExpansion' in text
    assert 'SOURCE=!SOURCE:\\=/!' in text
print('Windows path-with-spaces regression check passed.')
