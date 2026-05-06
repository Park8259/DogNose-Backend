import json
import math
import urllib.error
import urllib.request

BASE_URL = "http://localhost:6333"
COLLECTION = "dog_nose_embeddings"
VECTOR_SIZE = 2048


def request(method, path, body=None):
    data = None if body is None else json.dumps(body).encode("utf-8")
    req = urllib.request.Request(
        f"{BASE_URL}{path}",
        data=data,
        method=method,
        headers={"Content-Type": "application/json"},
    )
    try:
        with urllib.request.urlopen(req, timeout=5) as res:
            raw = res.read().decode("utf-8")
            return json.loads(raw) if raw else {}
    except urllib.error.HTTPError as exc:
        raw = exc.read().decode("utf-8")
        raise RuntimeError(f"{method} {path} failed: {exc.code} {raw}") from exc


def normalized_vector(seed):
    values = [((i + seed) % 17) / 17 for i in range(VECTOR_SIZE)]
    norm = math.sqrt(sum(value * value for value in values))
    return [value / norm for value in values]


def main():
    request(
        "PUT",
        f"/collections/{COLLECTION}",
        {"vectors": {"size": VECTOR_SIZE, "distance": "Cosine"}},
    )

    request(
        "PUT",
        f"/collections/{COLLECTION}/points",
        {
            "points": [
                {
                    "id": 1,
                    "vector": normalized_vector(seed=1),
                    "payload": {
                        "dogId": 1,
                        "nosePrintId": 1,
                        "imageUrl": "/uploads/reference-nose-1.jpg",
                        "ownerId": 1,
                        "embeddingModel": "s101_224",
                        "reference": True,
                    },
                },
                {
                    "id": 2,
                    "vector": normalized_vector(seed=8),
                    "payload": {
                        "dogId": 2,
                        "nosePrintId": 2,
                        "imageUrl": "/uploads/reference-nose-2.jpg",
                        "ownerId": 2,
                        "embeddingModel": "s101_224",
                        "reference": True,
                    },
                },
            ]
        },
    )

    result = request(
        "POST",
        f"/collections/{COLLECTION}/points/search",
        {"vector": normalized_vector(seed=1), "limit": 2, "with_payload": True},
    )

    print(json.dumps(result, ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()
