from pathlib import Path
p=Path(__file__).resolve().parents[1]/'data'/'resources.txt'
ids=set(); identifiers=set(); types=set(); rows=0; malformed=0
for line in p.read_text(encoding='utf-8').splitlines():
    rows+=1
    parts=line.split('|')
    if len(parts)!=11: malformed+=1; continue
    rid,rtype,_,_,identifier,*_=parts
    if rid in ids: raise SystemExit(f'duplicate id: {rid}')
    if identifier in identifiers: raise SystemExit(f'duplicate identifier: {identifier}')
    ids.add(rid); identifiers.add(identifier); types.add(rtype)
expected={'BOOK','JOURNAL','RESEARCH_PAPER','MAGAZINE'}
assert rows==50000, f'expected 50000 rows, got {rows}'
assert malformed==0, f'malformed rows: {malformed}'
assert types==expected, f'wrong types: {types}'
print(f'Dataset valid: {rows} rows, {len(ids)} unique IDs, {len(identifiers)} unique identifiers, types={sorted(types)}')
