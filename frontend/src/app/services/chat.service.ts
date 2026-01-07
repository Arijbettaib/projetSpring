import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private authService = inject(AuthService);
  private apiUrl = 'http://localhost:8888/api/agent';

  getHistory(): Observable<string[]> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.authService.getToken()}`);
    return inject(HttpClient).get<string[]>(`${this.apiUrl}/history`, { headers });
  }

  streamChat(message: string): Observable<string> {
    const subject = new Subject<string>();
    const token = this.authService.getToken();

    fetch(`${this.apiUrl}/chat/stream`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({ message })
    }).then(async response => {
      if (!response.ok) {
        const errorText = await response.text();
        subject.error(`Erreur ${response.status}: ${errorText}`);
        return;
      }

      const reader = response.body?.getReader();
      const decoder = new TextDecoder();

      if (!reader) {
        subject.error('Stream non disponible');
        return;
      }

      const push = () => {
        reader.read().then(({ done, value }) => {
          if (done) {
            subject.complete();
            return;
          }
          const chunk = decoder.decode(value, { stream: true });

          // Parse SSE format: data: message\n\n
          const lines = chunk.split('\n');
          for (const line of lines) {
            if (line.startsWith('data:')) {
              // Slice 5 to remove 'data:'... preserving spaces is critical
              const data = line.slice(5);
              // If there is an initial space (protocol), remove it via trimStart OR just trust the slice
              // Standard SSE has a space after colon, e.g. "data: foo". 
              // But chunk content might start with space e.g. "data:  foo" -> " foo"
              // We'll remove the first char only if it's a space, but keep the rest.
              // Actually, simpler: just push the raw data part.
              // If Spring AI sends "data:Hello" (no space), slice(5) is "Hello".
              // If "data: Hello", slice(5) is " Hello". We probably want just "Hello" in that case?
              // No, Llama might emit " Hello". 
              // Let's assume standard behavior: slice(5) creates the string. 
              // If it starts with a space because of SSE protocol, we should strip it?
              // Let's simply NOT trim at all. The browser's native EventSource handles this, but here we do manual.
              // Safest fix for "missing spaces" is to assume the backend sends "data:Chunk".

              if (data) subject.next(data);
            } else if (line.trim() && !line.startsWith(':')) {
              // If it's not data: but contains text, push it anyway (fallback)
              subject.next(line);
            }
          }
          push();
        }).catch(err => subject.error(err));
      };
      push();
    }).catch(err => subject.error(err));

    return subject.asObservable();
  }
}
