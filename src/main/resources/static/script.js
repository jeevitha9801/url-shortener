async function shortenUrl() {

    document.getElementById("message").innerText = "";

    const originalUrl =
        document.getElementById("urlInput").value;

    const customAlias =
        document.getElementById("aliasInput").value;

    try {

        const response = await fetch("/api/urls", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                originalUrl: originalUrl,
                customAlias: customAlias
            })
        });

        if (!response.ok) {

            const errorText = await response.text();

            document.getElementById("message").innerText =
                errorText;

            document.getElementById("result").innerHTML = "";

            return;
        }

        const data = await response.json();

        document.getElementById("result").innerHTML = `
            <p style="color:white;margin-bottom:15px;">
                Generated Link
            </p>

            <div class="link-box">
                ${data.shortUrl}
                </a>
            </div>

            <br>

            <button class="copy-btn"
                    onclick="copyUrl('${data.shortUrl}')">
                Copy
            </button>

            <button class="copy-btn"
                    onclick="openLink('${data.shortUrl}')">
                Open
            </button>
            <button class="copy-btn"
                    onclick="showAnalytics('${data.shortUrl}')">
                Analytics
            </button>
            <button class="copy-btn"
                    onclick="showTopUrls()">
                Top URLs
            </button>
        `;

    } catch (error) {

        document.getElementById("message").innerText =
            "Something went wrong. Please try again.";
    }
}

function copyUrl(url){

    navigator.clipboard.writeText(url);

    const toast = document.getElementById("toast");

    toast.classList.add("show");

    setTimeout(() => {
        toast.classList.remove("show");
    }, 3000);
}

async function openLink(url) {

    const shortCode =
        url.substring(url.lastIndexOf("/") + 1);

    const response = await fetch(
        `/api/urls/${shortCode}/status`
    );

    if(response.ok){

        window.open(url, "_blank");

    } else {

        document.getElementById("expiredModal")
            .style.display = "block";
    }
}

function closeModal(){
    document.getElementById("expiredModal")
        .style.display = "none";
}
async function showAnalytics(url){

    document.getElementById("topUrlsContainer").innerHTML = "";

    const shortCode =
        url.substring(url.lastIndexOf("/") + 1);

    const response =
        await fetch(`/api/urls/${shortCode}/analytics`);

    const data =
        await response.json();

    document.getElementById("analyticsContainer").innerHTML = `
        <div class="table-card">

            <h3>📊 URL Analytics</h3>

            <table>
                <tr>
                    <th>Short Code</th>
                    <td>${data.shortCode}</td>
                </tr>

                <tr>
                    <th>Original URL</th>
                    <td>${data.originalUrl}</td>
                </tr>

                <tr>
                    <th>Click Count</th>
                    <td>${data.clickCount}</td>
                </tr>

                <tr>
                    <th>Created At</th>
                    <td>${data.createdAt}</td>
                </tr>

                <tr>
                    <th>Expires At</th>
                    <td>${data.expiresAt}</td>
                </tr>

            </table>

        </div>
    `;
}

async function showTopUrls(){

    document.getElementById("analyticsContainer").innerHTML = "";

    const response =
        await fetch('/api/urls/top');

    const urls =
        await response.json();

    let rows = '';

    urls.forEach((url,index) => {

        rows += `
<tr>
<td>${index + 1}</td>
<td>${url.shortCode}</td>
<td>${url.clickCount}</td>
</tr>
`;
    });

    document.getElementById("topUrlsContainer").innerHTML = `
        <div class="table-card">

            <h3>🔥 Top 3 Most Visited URLs</h3>

            <table>

                <tr>
                    <th>Rank</th>
                    <th>Short Code</th>
                    <th>Clicks</th>
                </tr>

                ${rows}

            </table>

        </div>
    `;
}