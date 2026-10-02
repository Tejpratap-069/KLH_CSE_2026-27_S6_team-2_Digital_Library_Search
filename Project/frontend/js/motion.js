(() => {
  const reduce = window.matchMedia && window.matchMedia('(prefers-reduced-motion: reduce)').matches;
  const reveal = () => {
    const items = document.querySelectorAll('.fade-in,.reveal');
    if (reduce || !('IntersectionObserver' in window)) { items.forEach(el => el.classList.add('is-visible')); return; }
    const io = new IntersectionObserver(entries => { entries.forEach(entry => { if (entry.isIntersecting) { entry.target.classList.add('is-visible'); io.unobserve(entry.target); } }); }, { threshold: .08, rootMargin: '0px 0px -25px' });
    items.forEach(el => io.observe(el));
  };
  const parallax = () => {
    const scene = document.querySelector('.knowledge-scene');
    if (!scene || reduce) return;
    const shell = document.querySelector('.experience-shell');
    shell?.addEventListener('pointermove', e => { const r = shell.getBoundingClientRect(); const x = (e.clientX-r.left)/r.width-.5; const y=(e.clientY-r.top)/r.height-.5; scene.style.setProperty('--mx',`${x*10}px`); scene.style.setProperty('--my',`${y*10}px`); scene.style.translate='var(--mx) var(--my)'; });
    shell?.addEventListener('pointerleave',()=>{scene.style.translate='0 0';});
  };
  document.addEventListener('DOMContentLoaded',()=>{reveal();parallax();document.querySelectorAll('.card,.btn').forEach(el=>el.addEventListener('pointerdown',()=>el.classList.add('pressed')));document.addEventListener('pointerup',()=>document.querySelectorAll('.pressed').forEach(el=>el.classList.remove('pressed')));});
  window.DLReveal=reveal;
})();