from pathlib import Path
root=Path(__file__).resolve().parents[1]
for required in ['README.md','run.bat','run.sh','data/resources.txt','frontend/index.html','backend/src/digitallibrary/Main.java']:
    assert (root/required).exists(), f'missing required deliverable: {required}'
for forbidden in ['pom.xml','build.gradle','package.json']:
    assert not (root/forbidden).exists(), f'unexpected dependency manifest: {forbidden}'
print('Project contract checks passed')
